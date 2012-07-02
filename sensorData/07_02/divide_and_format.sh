#!/bin/bash
if [ -z $1 ] ; then
    echo "taking default file: sensorData.txt"
    file='sensorData.txt'
else
    echo "splitting file $1"
    file=$1
fi

if [ -d splitted ] ; then
echo "directory already exists"
else
echo "creating directory"
mkdir splitted
fi
for i in 101 102 103 104 \\\/android\\\/brenner
do
   echo "creating File for Sensor: $i"
   tmp=`echo "$i" | sed -e 's/\\\\\//_/g'`
   cat $file | grep "sender: $i" > splitted/$tmp
   sed -e "s/AccelerometerEvent sender: $i, time: //g" splitted/$tmp > splitted/tmp
   sed -e "s/, x://g" splitted/tmp > splitted/$tmp
   sed -e "s/, y://g" splitted/$tmp > splitted/tmp
   sed -e "s/, z://g" splitted/tmp > splitted/$tmp
   sed -e "s/  / /g" splitted/$tmp > splitted/tmp
   mv splitted/tmp splitted/$tmp
   

#rm splitted/tmp
done
echo "creating Tagging-Gui file"
cat $file | grep "tagging-Gui" > splitted/tagging-Gui
sed -e "s/TaggingEvent sender: tagging-Gui, time: //g" splitted/tagging-Gui > splitted/tmp
sed -e "s/, message://g" splitted/tmp > splitted/tagging-Gui
rm splitted/tmp