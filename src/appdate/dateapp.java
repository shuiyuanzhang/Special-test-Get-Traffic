package appdate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class dateapp {

	public static void main(String[] args) throws Throwable {
		totalDataAll();
	}
		
	//输入待测试的Activity
	public static String packageName() {
		String pStringActivity = "com.android.updater";
//		String pStringActivity="tran.com.android.taplaota";
		return pStringActivity;
	}
	
		
	/**
	 * 获取UId
	 * 
	 * Created on 2016/06/06
	 * 
	 * @author Main.z
	 * @throws IOException 
	 *
	 */   
	public static String getAPPUid() throws IOException  {
		Process getUidcmd = Runtime.getRuntime().exec("adb shell dumpsys package "+packageName());
		InputStream in = getUidcmd.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));		
		String pString =null;
		while ((pString=br.readLine())!=null) {
			if (pString.contains("userId")) {
				if (pString.length()>23) {
					return pString.trim().split(" ")[0].split("=")[1].trim();
				}
			}			
		}		
		br.close();
		return null;
	}
	
	/**
	 * 获取TCP_RCV流量值
	 * 
	 * Created on 2016/06/06
	 * 
	 * @author Main.z
	 *
	 */ 
	public static Integer tcp_rcv() {
		Process tcp_rcv_process;
		Integer tcp_rcv_data = null;
		try {
			tcp_rcv_process = Runtime.getRuntime().exec("adb shell cat /proc/uid_stat/"+getAPPUid()+"/tcp_rcv");
			InputStream in = tcp_rcv_process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			tcp_rcv_data =Integer.parseInt(br.readLine());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tcp_rcv_data;
	}
	
	
	/**
	 * 获取TCP_SND流量值
	 * 
	 * Created on 2016/06/06
	 * 
	 * @author Main.z
	 *
	 */  
	public static Integer tcp_snd()  {
		Process tcp_snd_process;
		Integer tcp_snd_data = null;
		try {
			tcp_snd_process = Runtime.getRuntime().exec("adb shell cat /proc/uid_stat/"+getAPPUid()+"/tcp_snd");
			InputStream in = tcp_snd_process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			tcp_snd_data =Integer.parseInt(br.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return tcp_snd_data;
	}
	
	/**
	 * 计算启动流量
	 * 
	 * Created on 2016/06/20
	 * 
	 * @author Main.z
	 * @throws IOException 
	 *
	 */  
	public static void totalDataAll() throws IOException {
		for(int i=0;i<10;i++){
			Runtime.getRuntime().exec("adb shell am start com.android.updater/.MainActivity");
			int total_data_befor= tcp_rcv()+tcp_snd();
			sleep(2000);
			Runtime.getRuntime().exec("adb shell pm clear "+packageName());			
			int total_data_after=tcp_rcv()+tcp_snd();
			int total_data_all=total_data_after-total_data_befor;
			System.out.println(total_data_all);
						
		}
	}
	
	
	/**
	 * 休眠时间
	 * 
	 * Created on 2016/06/20
	 * 
	 * @author Main.z
	 * @throws IOException 
	 *
	 */ 
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
