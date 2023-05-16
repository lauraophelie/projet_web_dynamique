javac -parameters -d . Framework/*.java

jar cvfm framework.jar MANIFEST.MF etu1885/framework/*.class etu1885/*.class  etu1885/framework/servlet/*.class

rmdir "D:\IT University\S4\Web_Dynamique\projet_web_dynamique\etu1885" -Recurse

mkdir D:\tempTestFramework

cd D:\tempTestFramework\

mkdir WEB-INF

cd WEB-INF/

mkdir lib

mkdir classes

Copy-Item "D:\IT University\S4\Web_Dynamique\projet_web_dynamique\framework.jar" "D:\tempTestFramework\WEB-INF\lib\" -Recurse

Copy-Item "D:\IT University\S4\Web_Dynamique\Test\test" "D:\tempTestFramework\WEB-INF\classes\" -Recurse

Copy-Item "D:\IT University\S4\Web_Dynamique\projet_web_dynamique\TestFramework\*.jsp" "D:\tempTestFramework\"

cd D:\tempTestFramework

jar -cvf TestFramework.war *

cd D:\

Copy-Item "D:\tempTestFramework\TestFramework.war" "D:\Localhost\apache-tomcat-8.5.87\webapps"

rmdir "D:\tempTestFramework" -Recurse