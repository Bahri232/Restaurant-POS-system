# Script to sign installers and export signing certificate for GitHub Actions
$ErrorActionPreference = "Stop"

Write-Host "=== 1. Locating Publisher Certificate ==="
$cert = Get-ChildItem -Path Cert:\CurrentUser\My -CodeSigningCert | Where-Object { $_.Subject -match "Abdul Malik Barrie" } | Select-Object -First 1

if (-not $cert) {
    Write-Error "Certificate for Abdul Malik Barrie not found in Cert:\CurrentUser\My"
    exit 1
}

Write-Host "Found Certificate: $($cert.Subject) [Thumbprint: $($cert.Thumbprint)]"

# 2. Export PFX so GitHub Actions uses this exact certificate
$certsDir = Join-Path $PSScriptRoot "..\certs"
if (-not (Test-Path $certsDir)) {
    New-Item -ItemType Directory -Path $certsDir -Force | Out-Null
}

$pfxPath = Join-Path $certsDir "PublisherCert.pfx"
$pfxPassword = ConvertTo-SecureString -String "RenessPOS2026!" -Force -AsPlainText
Export-PfxCertificate -Cert $cert -FilePath $pfxPath -Password $pfxPassword | Out-Null
Write-Host "Exported PFX certificate to: $pfxPath"

# Also ensure public .cer is updated in repo root
$cerPath = Join-Path $PSScriptRoot "..\AbdulMalikBarrie_Publisher.cer"
$certBytes = $cert.Export([System.Security.Cryptography.X509Certificates.X509ContentType]::Cert)
[System.IO.File]::WriteAllBytes($cerPath, $certBytes)
Write-Host "Updated public certificate at: $cerPath"

# 3. Find any RenessPOS_Setup.exe on the system (Temp, Downloads, Documents, Desktop)
Write-Host "`n=== 2. Locating and Signing Downloaded Installers ==="
$searchPaths = @(
    $env:TEMP,
    [System.Environment]::GetFolderPath("MyDocuments"),
    [System.Environment]::GetFolderPath("Desktop"),
    (Join-Path $env:USERPROFILE "Downloads")
)

$foundInstallers = @()
foreach ($dir in $searchPaths) {
    if (Test-Path $dir) {
        $files = Get-ChildItem -Path $dir -Filter "*RenessPOS*.exe" -Recurse -Depth 4 -ErrorAction SilentlyContinue
        if ($files) {
            $foundInstallers += $files
        }
    }
}

foreach ($item in $foundInstallers) {
    Write-Host "Signing: $($item.FullName)"
    try {
        # Clear ReadOnly attribute if set by Windows zip extractor
        if ($item.IsReadOnly) {
            $item.IsReadOnly = $false
        }
        # Remove Mark-of-the-Web (Zone.Identifier)
        Unblock-File -Path $item.FullName -ErrorAction SilentlyContinue
        
        # Sign the executable
        $sig = Set-AuthenticodeSignature -FilePath $item.FullName -Certificate $cert -HashAlgorithm SHA256
        Write-Host "  -> Result: $($sig.Status)"
        
        # Verify
        $verify = Get-AuthenticodeSignature -FilePath $item.FullName
        Write-Host "  -> Verified Signer: $($verify.SignerCertificate.Subject)"
    } catch {
        Write-Warning "Could not sign $($item.FullName): $_"
    }
}

Write-Host "`n=== 3. Done! All detected installers are signed and unblocked ==="
