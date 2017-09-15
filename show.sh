#!/bin/bash 
 
function show()
{
cd $1
for file in `ls`
do
if [ -d "$file" ];then
    show "$file"
else
#    echo "$file"
path=$file

subpixf5=${path:((${#path} - 5))}
subpixf4=${path:((${#path} - 4))}
subpixf3=${path:((${#path} - 3))}
subpixf2=${path:((${#path} - 2))}

	#echo "file-len = "${#path}", name = "${subpixf4}
if [ ${#path} -ge 5 ] ;then
if [ x$subpixf5 = x".java" ] ;then
    echo $PWD"/"$path
    cat "$file" > "$file".txt
    rm $file
    mv $file.txt $file
#    readlink -f $file
fi
fi

if [ ${#path} -ge 4 ] ;then
if [ x$subpixf4 = x".cpp" ] ;then
    echo $PWD"/"$path
    cat "$file" > "$file".txt
    rm $file
    mv $file.txt $file
#    readlink -f $file
fi
fi

if [ ${#path} -ge 3 ] ;then
if [ x$subpixf3 = x".cc" ] ;then
    echo $PWD"/"$path
    cat "$file" > "$file".txt
    rm $file
    mv $file.txt $file
#    readlink -f $file
fi
fi

if [ ${#path} -ge 2 ] ;then
if [ x$subpixf2 = x".c" ] || [ x$subpixf2 = x".h" ] ;then
    echo $PWD"/"$path
    cat "$file" > "$file".txt
    rm $file
    mv $file.txt $file
#    readlink -f $file

fi

if [ x$subpixf2 = x".C" ] || [ x$subpixf2 = x".H" ] ;then
    echo $PWD"/"$path
    cat "$file" > "$file".txt
    rm $file
    mv $file.txt $file
    chmod 775 $file
    readlink -f $file

fi

fi

#echo "$file"
#readlink -f $file
fi
done 
cd ..
}
 
show $1
 
exit 0
