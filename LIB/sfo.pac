/* ---------------------------------------------------------- 	*/
/* Created and Modified by Integ-Nsec				*/
/* Version 3.1  						*/
/* Last Modified : 27 Apr 2017					*/
/* ---------------------------------------------------------- 	*/

function FindProxyForURL(url, host)
{
// variable strings to return
var proxyair = "PROXY usaplaintpxy01.ad.infosys.com:80;PROXY 10.156.65.41:80;PROXY usabosintpxy01.ad.infosys.com:80;PROXY 192.168.165.210:80";
var proxyvod = "PROXY usaplaintpxy01.ad.infosys.com:80;PROXY 10.156.65.41:80;PROXY usabosintpxy01.ad.infosys.com:80;PROXY 192.168.165.210:80";
var proxy_no = "DIRECT";

url = url.toLowerCase();
host = host.toLowerCase();

/*Redirection to Local Proxy for Generic Internet URL access*/
	{
                if 	(    	dnsDomainIs(host, "launchpad.infosys.com")||
				dnsDomainIs(host, "enterpriseregistration.windows.net")||
				dnsDomainIs(host, "login.microsoftonline.com")
			)
                	return proxyvod;

		else if	(	isPlainHostName(host)||
				dnsDomainIs(host, "ad.infosys.com")||
				dnsDomainIs(host, "is.infosys.com")||
				dnsDomainIs(host, "iconnect.infosys.com")||
				dnsDomainIs(host, "iconnectodc.infosys.com")||
				dnsDomainIs(host, "usaplainfdrv01.infosys.com")||
				dnsDomainIs(host, "usaplainfdrv02.infosys.com")||
				dnsDomainIs(host, "blrkecinfdrv01.infosys.com")||
				dnsDomainIs(host, "blrkecinfdrv02.infosys.com")||
				dnsDomainIs(host, "infydrive.infosys.com")||
				dnsDomainIs(host, "apps.infosys.com")||
				dnsDomainIs(host, "evapps.infosys.com")||
				dnsDomainIs(host, "ocsp.infosys.com")||
				dnsDomainIs(host, "akaash.infosys.com")||
				dnsDomainIs(host, "stsakaash.infosys.com")||
				shExpMatch(url, "http://10.*.*.*")||
				shExpMatch(url, "https://10.*.*.*")||
				shExpMatch(url, "*172.21.*.*")||
				shExpMatch(url, "*172.22.*.*")||
				shExpMatch(url, "*172.23.*.*")||
				shExpMatch(url, "*172.24.*.*")||
				shExpMatch(url, "*172.25.*.*")||
				shExpMatch(url, "*172.26.*.*")||
				shExpMatch(url, "*172.31.*.*")||
				shExpMatch(url, "*127.0.*.*")||
				shExpMatch(url, "*localhost*")||
				shExpMatch(url, "*.local*")||
				shExpMatch(url, "http://blrkecsdb*")||
				shExpMatch(url, "*ad.infosys.com/*")||
				shExpMatch(url, "*infyucce.com*")||
				shExpMatch(url, "http://blrkec*")||
				shExpMatch(url, "*ppc.infosys.com/*")||
				dnsDomainIs(host, "remote-idemo-ppc.infosys.com")||
				shExpMatch(url, "*192.168.*.*")
			)
			return "DIRECT";

		else if (	shExpMatch(url, "*autodiscover-s.outlook.com*")||
				shExpMatch(url, "*outlook.office365.com*")
			)
			return proxyvod;

                else if (    dnsDomainIs(host, "google.com")||
			     dnsDomainIs(host, "google.co.in")||
			     dnsDomainIs(host, "bing.com")||
			     dnsDomainIs(host, "yahoo.com")||
			     dnsDomainIs(host, "yahoo.co.in")||
			     dnsDomainIs(host, "amazon.in")||
                             shExpMatch(url, "*flipkart*")
			)
	                return proxyvod;

		else if (	shExpMatch(url, "*meet.infosys.com*")||
				dnsDomainIs(host, "*meet.infosys.com*")||
				shExpMatch(url, "*dialin.infosys.com*")||
				shExpMatch(url, "*-int.infosys.com*")||
				shExpMatch(url, "*-int01.infosys.com*")||
				shExpMatch(url, "*-int02.infosys.com*")||
				shExpMatch(url, "*autodiscover.infosys.com*")||
				shExpMatch(url, "*-int03.infosys.com*")||
				shExpMatch(url, "*ls01wac.infosys.com*")||
				shExpMatch(url, "*ls03wac.infosys.com*")||
				shExpMatch(url, "*webvc.infosys.com*")||
				shExpMatch(url, "*lyncdiscoverinternal.infosys.com*")||
				shExpMatch(url, "https://*.ad.infosys.com*")||
				shExpMatch(url, "*vc.infosys.com*")
			)
			return proxy_no;

		else if (	shExpMatch(url, "*microsoft*")||
				shExpMatch(url, "*bank*")||
				shExpMatch(url, "*support*")||
				shExpMatch(url, "*connect*")||
				shExpMatch(url, "*webex*")||
				shExpMatch(url, "*meet*")||
				shExpMatch(url, "*amazon*")||
				shExpMatch(url, "*azure*")||
				shExpMatch(url, "*money*")||
				shExpMatch(url, "*share*")||
				shExpMatch(url, "*download*")||
				shExpMatch(url, "*youtube*")||
				shExpMatch(url, "*linked*")||
				shExpMatch(url, "*infosys*")||
				shExpMatch(url, "*video*")||
				shExpMatch(url, "*times*")||
				shExpMatch(url, "*news*")||
				shExpMatch(url, "*yammer*")||
				shExpMatch(url, "*apple*")||
				shExpMatch(url, "*oracle*")||
				shExpMatch(url, "*google*")||
				shExpMatch(url, "*proxy*")||
				shExpMatch(url, "*ndtv*")||
				shExpMatch(url, "*cric*")
			)
			return proxyvod;

		else  
	
			return proxyvod;
	}
}
