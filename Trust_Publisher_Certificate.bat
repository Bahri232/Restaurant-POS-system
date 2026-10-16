@echo off
echo ========================================================
echo   Reness POS - Trust Publisher Certificate
echo   Publisher: Abdul Malik Barrie (BAHRI)
echo ========================================================
echo.
echo Installing certificate into Windows Trusted Stores...
echo If Windows displays a Security Warning asking:
echo   "Do you want to install this certificate?"
echo Please click [ YES ] to confirm.
echo.
certutil -user -addstore Root "%~dp0AbdulMalikBarrie_Publisher.cer"
certutil -user -addstore TrustedPublisher "%~dp0AbdulMalikBarrie_Publisher.cer"
echo.
echo ========================================================
echo   Installation Complete!
echo   Windows now recognizes Abdul Malik Barrie (BAHRI)
echo   as the official Verified Publisher for Reness POS.
echo ========================================================
echo.
pause
