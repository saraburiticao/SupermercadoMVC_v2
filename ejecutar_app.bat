@echo off
echo ==========================================
echo   SupermercadoMVC - Sistema de Empleados
echo ==========================================
echo.
echo Iniciando aplicacion...
echo.

cd /d "%~dp0"
java -cp "bin;lib\mysql-connector-j-8.0.33.jar" vista.EmpleadoTableView

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo iniciar la aplicacion.
    echo Verifica que MySQL este ejecutandose.
    pause
)

