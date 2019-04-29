#!/bin/bash
SOURCE="${BASH_SOURCE[0]}"
DIR="$( dirname "$SOURCE" )"
while [ -h "$SOURCE" ]
do 
  SOURCE="$(readlink "$SOURCE")"
  echo $SOURCE
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
  DIR="$( cd -P "$( dirname "$SOURCE"  )" && pwd )"
  echo $DIR
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

cd "$DIR"
java -jar wechat4j.jar "(车寻人)*.*(((晚\w*|今\w*晚\w*)+(8|20))|20:)+.*(十里河|分钟寺|九龙山|平乐园|将台)+"