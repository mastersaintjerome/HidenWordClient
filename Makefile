all :
    mkdir build
    javac -d build $$(find src/ -type f -follow -print)

run : 
    java -classpath build App.View.ClientInterface
    
clean :
    rm -r build