# TGFC
自娱自乐，用来实验各种功能。


##项目介绍

###简介
* 本项目为[TGFC俱乐部](http://club.tgfcer.com/)论坛的安卓手机客户端。

###实现原理
* 因为不知道网站的api，所以只能直接从网页抓取数据。
* 论坛首页：发送请求，获取整个网页的html字符串。利用正则取出论坛各版块名和相应链接，然后用listview显示。
* 帖子列表：同理用上一步取出的链接获取帖名、对应链接、回复数、页数并加以显示。
* 帖子内容：同理用正则获取id、时间、回复部分html字符，回复部分的html字符用HtmlFrom重新解析成相应格式。

###已实现功能
* 查看论坛各板块
* 查看各板块帖子及回复数
* 查看帖子内容，各楼层id、回复时间、内容
* 自动加载，手动刷新

##截图
![首页](http://ww3.sinaimg.cn/mw690/74de6eafgw1eue9pjgbv1j20k00zkacx.jpg)  ![板块](http://ww3.sinaimg.cn/mw690/74de6eafgw1eue9pkq5ovj20k00zk79a.jpg)
![帖子1](http://ww3.sinaimg.cn/mw690/74de6eafgw1eue9pm1al3j20k00zkn3x.jpg)
![帖子2](http://ww2.sinaimg.cn/mw690/74de6eafgw1eue9pna5oxj20k00zktd8.jpg)
![帖子3](http://ww1.sinaimg.cn/mw690/74de6eafgw1eue9poknfxj20k00zkgq6.jpg)
  
