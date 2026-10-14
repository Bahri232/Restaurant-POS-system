[Setup]
AppName=Reness POS
AppVersion=1.0
DefaultDirName={pf}\Reness POS
DefaultGroupName=Reness POS
OutputDir=target\installer
OutputBaseFilename=RenessPOS_Installer
Compression=lzma2
SolidCompression=yes
ArchitecturesInstallIn64BitMode=x64
DisableProgramGroupPage=yes

[Files]
Source: "target\floreantpos-bin\floreantpos\floreantpos.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\floreantpos.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\floreantpos-bin\floreantpos\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "database\derby-single\posdb\*"; DestDir: "{app}\database\posdb"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\config\*"; DestDir: "{app}\config"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\i18n\*"; DestDir: "{app}\i18n"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\floreantpos-bin\floreantpos\plugins\*"; DestDir: "{app}\plugins"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Reness POS"; Filename: "{app}\floreantpos.exe"
Name: "{commondesktop}\Reness POS"; Filename: "{app}\floreantpos.exe"; Tasks: desktopicon

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Dirs]
Name: "{app}"; Permissions: users-modify
