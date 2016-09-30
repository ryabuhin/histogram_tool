#!/bin/bash
i="-i"
o="-o"
#Way to the bash-script and executable .JAR
JAR_WAY=`dirname $0`
JAR_FOLDER=`dirname $JAR_WAY`
value=false

if [ "$1" = "$i" -a "$3" = "$o" ]; then
	INPUT_IMAGE_WAY=$2
	OUTPUT_IMAGE_WAY=$4
	value=true
fi
if [ "$1" = "$o" -a "$3" = "$i" ]; then
	INPUT_IMAGE_WAY=$4
	OUTPUT_IMAGE_WAY=$2
	value=true
fi

if [ "$value" != true ]; then exit
fi

#Copy user image to directory with executable .JAR-FILE	
cp $INPUT_IMAGE_WAY $JAR_FOLDER
cd $JAR_FOLDER
jar xvf $JAR_WAY/imhist.jar
jar -cmf ./META-INF/MANIFEST.MF ./imhistTemp.jar ./ua/ryabuhin_valentine/*.class ./template.jpg ./`basename $INPUT_IMAGE_WAY` ./META-INF/maven/*
rm -rf ./META-INF/
rm -rf ./ua/
rm ./`basename $INPUT_IMAGE_WAY`
rm ./template.jpg

#Executing config .JAR-FILE(imhistTemp.jar)
java -jar imhistTemp.jar -i `basename $INPUT_IMAGE_WAY` -o `basename $4`

#Delete temp executable.jar
rm ./imhistTemp.jar
#Move complete histogram by the OUTPUT_IMAGE_WAY
mv ./`basename $OUTPUT_IMAGE_WAY` $OUTPUT_IMAGE_WAY
exit


