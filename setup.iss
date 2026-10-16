[Setup]
AppName=Reness POS
AppVersion=1.5.0
AppPublisher=Abdul Malik Barrie (BAHRI)
AppPublisherURL=mailto:abdulmalikbarrie@gmail.com
AppSupportURL=mailto:abdulmalikbarrie@gmail.com
AppUpdatesURL=mailto:abdulmalikbarrie@gmail.com
DefaultDirName={pf}\Reness POS
DefaultGroupName=Reness POS
OutputDir=target\installer
OutputBaseFilename=RenessPOS_Installer
Compression=lzma2
SolidCompression=yes
ArchitecturesInstallIn64BitMode=x64
DisableProgramGroupPage=yes
SetupIconFile=resources\icons\icon.ico
UninstallDisplayIcon={app}\icon.ico
UninstallDisplayName=Reness POS
VersionInfoCompany=Abdul Malik Barrie (BAHRI)
VersionInfoDescription=Reness POS Setup
VersionInfoVersion=1.5.0.0
VersionInfoCopyright=Copyright (C) 2026 Abdul Malik Barrie (BAHRI)
VersionInfoProductName=Reness POS

[Files]
Source: "resources\icons\icon.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\floreantpos.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\RenessPOS.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\floreantpos.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "database\derby-single\posdb\*"; DestDir: "{app}\database\posdb"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\config\*"; DestDir: "{app}\config"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\i18n\*"; DestDir: "{app}\i18n"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\plugins\*"; DestDir: "{app}\plugins"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Reness POS"; Filename: "{app}\RenessPOS.exe"; IconFilename: "{app}\icon.ico"
Name: "{commondesktop}\Reness POS"; Filename: "{app}\RenessPOS.exe"; Tasks: desktopicon; IconFilename: "{app}\icon.ico"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: checkedonce

[Dirs]
Name: "{app}"; Permissions: users-modify
