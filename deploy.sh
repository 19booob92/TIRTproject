sudo pkill -9 -f jetty
sudo pkill -9 -f tomcat

mvn clean install -Dmaven.test.skip=true
cd /home/mateusz/Programy/apache-tomcat-8.0.15/webapps/
sudo rm -fr *
cd /home/mateusz/Repos/TIRTproject/target/
sudo mv tirt-0.0.1-SNAPSHOT.war ROOT.war
sudo cp /home/mateusz/Repos/TIRTproject/target/ROOT.war /home/mateusz/Programy/apache-tomcat-8.0.15/webapps/

sudo sh /home/mateusz/Programy/apache-tomcat-8.0.15/bin/startup.sh


tail -f /home/mateusz/Programy/apache-tomcat-8.0.15/logs/catalina.out
