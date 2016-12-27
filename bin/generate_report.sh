#!/bin/sh
#
#Author: chenjun
#Date:2016-12-30
#Description:
#Usage:
#

source /etc/profile


#init parameters
function init()
{
  theYear=`date '+%Y'`
  echo $theYear

  theDay="$1"
  if [ "$theDay" = "" ]
  then 
    theDay=`date +%Y-%m-%d` 
  fi
  theDayYesterday=`date +%Y-%m-%d -d "$theDay 1 days ago "`
  echo $theDayYesterday"===="$theDay
  echo "-----------------------------------------" 
}

function executeSql()
{
  sql="$1"
  
  if [ "$sql" = "" ]
  then
    cat | mysql -uroot  -N 
  else
    echo "$sql" | mysql -uroot  -N
  fi
}

#SaleKIPReport
# generate by day
function saleKIPReport()
{
  sql="use dairui_crm;replace into SaleKIPReport(writedate, channelName, deptName, realsaletotal, realorderId,
  merchandiseName, orderRate, cPrice, hPrice, delivery) 
  select $theDayYesterday,p.channelIdText,d.deptName, sum(distinct p.ordertotal)ordertotal（实际销售额）,
  count(distinct p.orderid)totalOrderId,count(m.merchandiseName) merchandiseName,
  (count(m.merchandiseName)/ count(DISTINCT(m.orderid)))orderRate, 
  (sum(distinct p.ordertotal) div count(distinct(realname))) cPrice,
  (sum(distinct p.ordertotal) div count(distinct p.orderid)) hPrice,
  (sum(distinct p.delivery=2)/count(distinct p.delivery)) delivery 
  from PaymentOrder p,MerchandiseMaterial m,WorkerGroup g ,Dept d 
  where p.orderid=m.orderid and p.channelId=g.groupId and  (p.support<>'作废' or p.support is null) 
  and  p.channelId>0   and  p.status<>'900'  and p.addtime>=$theDayYesterday 
  AND d.deptId=g.deptId and p.addtime<$theDay group by p.channelId;"
  echo $sql
  
  executeSql "$sql"
}

#SaleSKUReport
# generate by day
function saleSKUReport()
{
  sql="use dairui_crm;replace into SaleSKUReport(writedate, channelName, deptName, ordertotal, wRing, ring, ornament) 
  select $theDayYesterday,p.channelIdText, d.deptName, sum(distinct p.ordertotal)ordertotal,sum(if(merchandisesTag='求婚钻戒',price,0))wRing ,
  sum(if(merchandisesTag in('对戒子类','男戒'),price,0))ring, sum(if(merchandisesTag in('套链','手链','耳环/耳钉','项链'),price,0))ornament 
  from PaymentOrder p,MerchandiseMaterial m,WorkerGroup g,Dept d  where p.orderid=m.orderid and p.channelId=g.groupId 
  and  (p.support<>'作废' or p.support is null) and  p.channelId>0   and  p.status<>'900'  and p.addtime>=$theDayYesterday 
  AND d.deptId=g.deptId and p.addtime<$theDay group by p.channelId;"
  echo $sql
  
  executeSql "$sql"
}

#SaleobjectiveReport
# generate by day
function saleobjectiveReport()
{
  sql="use dairui_crm;replace into SaleobjectiveReport(writedate, channelName, deptName, target, ordertotal, saleYearTarget) 
  select $theDay writedate, p.channelIdText channelName, d.deptName, t.target, sum(distinct p.ordertotal) ordertotal, s.yeartarget saleYearTarget, 
  (select sum(distinct p2.ordertotal) ordertotal from PaymentOrder p2 
  where (p2.support<>'作废' or p2.support is null) and  p2.channelId>0 and  p2.status<>'900' group by p2.channelId HAVING p2.channelId=p.channelId) yeartotal 
  from PaymentOrder p, SaleYearTarget s, SaleTarget t, Dept d 
  where p.channelId=s.groupId and s.writemonth=$theYear and s.deptId=t.deptId and t.writemonth='$theYear' and t.deptId=d.deptId 
  and (p.support<>'作废' or p.support is null) and  p.channelId>0 and  p.status<>'900'  group by p.channelId;"
  echo $sql
  
  executeSql "$sql"
}

function main()
{
  init "$1"
  saleKIPReport
  saleSKUReport
  saleobjectiveReport
}
main "$1"