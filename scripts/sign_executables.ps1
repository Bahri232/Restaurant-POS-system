# Script to digitally sign Reness POS executables with Abdul Malik Barrie (BAHRI)
param(
    [string]$CertSubject = "CN=Abdul Malik Barrie (BAHRI), O=Reness POS, E=abdulmalikbarrie@gmail.com"
)

$ErrorActionPreference = "Stop"

Write-Host "=== 1. Checking or Locating Code Signing Certificate for '$CertSubject' ==="

$cert = Get-ChildItem -Path Cert:\CurrentUser\My -CodeSigningCert | Where-Object { $_.Subject -match "Abdul Malik Barrie" } | Select-Object -First 1

if (-not $cert) {
    Write-Host "Creating new Code Signing certificate..."
    $cert = New-SelfSignedCertificate `
        -Type CodeSigningCert `
        -Subject $CertSubject `
        -FriendlyName "Abdul Malik Barrie (BAHRI) - Reness POS" `
        -KeyAlgorithm RSA `
        -KeyLength 2048 `
        -CertStoreLocation "Cert:\CurrentUser\My" `
        -NotAfter (Get-Date).AddYears(10)
    Write-Host "Created certificate: $($cert.Thumbprint)"
} else {
    Write-Host "Found existing certificate: $($cert.Thumbprint)"
}

Write-Host "=== 2. Exporting Public Certificate ==="
$cerPath = Join-Path $PSScriptRoot "..\AbdulMalikBarrie_Publisher.cer"
$certBytes = $cert.Export([System.Security.Cryptography.X509Certificates.X509ContentType]::Cert)
[System.IO.File]::WriteAllBytes($cerPath, $certBytes)
Write-Host "Public publisher certificate saved to: $cerPath"

Write-Host "=== 3. Adding to TrustedPublisher Store ==="
try {
    $pubStore = New-Object System.Security.Cryptography.X509Certificates.X509Store([System.Security.Cryptography.X509Certificates.StoreName]::TrustedPublisher, [System.Security.Cryptography.X509Certificates.StoreLocation]::CurrentUser)
    $pubStore.Open([System.Security.Cryptography.X509Certificates.OpenFlags]::ReadWrite)
    $certObj = New-Object System.Security.Cryptography.X509Certificates.X509Certificate2($cerPath)
    $pubStore.Add($certObj)
    $pubStore.Close()
    Write-Host "Added to CurrentUser\TrustedPublisher successfully."
} catch {
    Write-Warning "Could not automatically add to TrustedPublisher: $_"
}

Write-Host "=== 4. Digitally Signing Executables ==="
$exePaths = @(
    (Join-Path $PSScriptRoot "..\target\floreantpos-bin\floreantpos\floreantpos.exe"),
    (Join-Path $PSScriptRoot "..\target\floreantpos-bin\floreantpos\RenessPOS.exe"),
    (Join-Path $PSScriptRoot "..\dist\RenessPOS\floreantpos.exe"),
    (Join-Path $PSScriptRoot "..\dist\RenessPOS\RenessPOS.exe")
)

foreach ($exe in $exePaths) {
    if (Test-Path $exe) {
        Write-Host "Signing: $exe"
        $sig = Set-AuthenticodeSignature -FilePath $exe -Certificate $cert -HashAlgorithm SHA256
        Write-Host "  -> Result: $($sig.Status) ($($sig.StatusMessage))"
    } else {
        Write-Host "File not found (skipped): $exe"
    }
}

Write-Host "`n=== 5. Verifying Digital Signatures ==="
foreach ($exe in $exePaths) {
    if (Test-Path $exe) {
        $check = Get-AuthenticodeSignature -FilePath $exe
        $sub = if ($check.SignerCertificate) { $check.SignerCertificate.Subject } else { "None" }
        Write-Host "$([System.IO.Path]::GetFileName($exe)): Status=$($check.Status), Signer=$sub"
    }
}

Write-Host "`nSigning complete!"
