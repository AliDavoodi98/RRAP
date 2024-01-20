@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  flink-app startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and FLINK_APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\flink-app.jar;%APP_HOME%\lib\flink-clients_2.12-1.14.6.jar;%APP_HOME%\lib\flink-streaming-java_2.12-1.14.6.jar;%APP_HOME%\lib\flink-connector-kafka_2.12-1.14.6.jar;%APP_HOME%\lib\json-20210307.jar;%APP_HOME%\lib\flink-file-sink-common-1.14.6.jar;%APP_HOME%\lib\flink-optimizer-1.14.6.jar;%APP_HOME%\lib\flink-runtime-1.14.6.jar;%APP_HOME%\lib\flink-scala_2.12-1.14.6.jar;%APP_HOME%\lib\flink-java-1.14.6.jar;%APP_HOME%\lib\flink-rpc-akka-loader-1.14.6.jar;%APP_HOME%\lib\flink-hadoop-fs-1.14.6.jar;%APP_HOME%\lib\flink-core-1.14.6.jar;%APP_HOME%\lib\flink-queryable-state-client-java-1.14.6.jar;%APP_HOME%\lib\flink-shaded-guava-30.1.1-jre-14.0.jar;%APP_HOME%\lib\commons-math3-3.5.jar;%APP_HOME%\lib\kafka-clients-2.4.1.jar;%APP_HOME%\lib\flink-annotations-1.14.6.jar;%APP_HOME%\lib\flink-rpc-core-1.14.6.jar;%APP_HOME%\lib\slf4j-api-1.7.28.jar;%APP_HOME%\lib\jsr305-1.3.9.jar;%APP_HOME%\lib\flink-connector-base-1.14.6.jar;%APP_HOME%\lib\flink-metrics-core-1.14.6.jar;%APP_HOME%\lib\flink-shaded-force-shading-14.0.jar;%APP_HOME%\lib\commons-cli-1.3.1.jar;%APP_HOME%\lib\flink-shaded-asm-7-7.1-14.0.jar;%APP_HOME%\lib\commons-lang3-3.3.2.jar;%APP_HOME%\lib\kryo-2.24.0.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\commons-compress-1.21.jar;%APP_HOME%\lib\commons-io-2.8.0.jar;%APP_HOME%\lib\flink-shaded-netty-4.1.65.Final-14.0.jar;%APP_HOME%\lib\flink-shaded-jackson-2.12.4-14.0.jar;%APP_HOME%\lib\flink-shaded-zookeeper-3-3.4.14-14.0.jar;%APP_HOME%\lib\javassist-3.24.0-GA.jar;%APP_HOME%\lib\snappy-java-1.1.8.3.jar;%APP_HOME%\lib\lz4-java-1.8.0.jar;%APP_HOME%\lib\scala-compiler-2.12.7.jar;%APP_HOME%\lib\scala-reflect-2.12.7.jar;%APP_HOME%\lib\scala-xml_2.12-1.0.6.jar;%APP_HOME%\lib\scala-library-2.12.7.jar;%APP_HOME%\lib\chill_2.12-0.7.6.jar;%APP_HOME%\lib\zstd-jni-1.4.3-1.jar;%APP_HOME%\lib\minlog-1.2.jar;%APP_HOME%\lib\objenesis-2.1.jar;%APP_HOME%\lib\chill-java-0.7.6.jar

@rem Execute flink-app
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %FLINK_APP_OPTS%  -classpath "%CLASSPATH%" com.flink-app.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable FLINK_APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%FLINK_APP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
