sudo pkill -9 -f jetty
sudo pkill -9 -f tomcat

mvn clean install -Dmaven.test.skip=true
cd /home/booob/Programy/apache-tomcat-8.0.15/webapps/
sudo rm -fr *
cd /home/booob/Repos/TIRTproject/target/
sudo mv tirt-0.0.1-SNAPSHOT.war ROOT.war
sudo cp /home/booob/Repos/TIRTproject/target/ROOT.war /home/booob/Programy/apache-tomcat-8.0.15/webapps/

sudo sh /home/booob/Programy/apache-tomcat-8.0.15/bin/startup.sh


tail -f /home/booob/Programy/apache-tomcat-8.0.15/logs/catalina.out
