DEL "CeeFee*.exe"

::Build the Offline installer (Big)
COPY jre-6u13-windows-i586-p-s.exe "core files\jre.exe"
"%programfiles%\NSIS\makensis.exe" setup.nsi
DEL "core files\jre.exe"
REN "CeeFee FTP-?.?.exe" "CeeFee FTP-?.? OffLine.exe"

::Build the online installer (Small)
COPY jre-6u13-windows-i586-p-iftw.exe "core files\jre.exe"
"%programfiles%\NSIS\makensis.exe" setup.nsi
DEL "core files\jre.exe"
REN "CeeFee FTP-?.?.exe" "CeeFee FTP-?.? OnLine.exe"

@ECHO.
@ECHO.
@ECHO "Installers build succeefully!"
@ECHO.
@PAUSE
