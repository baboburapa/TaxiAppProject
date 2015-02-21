import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.StyledEditorKit.BoldAction;

/**
 * DateFormat = "yyyy-MM-dd HH:mm:ss"
 **/
public class MyDate {

	private final static String DateFormat = "yyyy-MM-dd HH:mm:ss";
	
	private SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
	private Calendar cal = Calendar.getInstance();
	
	private Date date;
	
	public MyDate(String dateTime) {
		
		try {
			
			date = sdf.parse(dateTime);
			cal.setTime(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Seconds Mode = 0, Minutes Mode = 1, Hours Mode = 2, Days Mode = 3, Default Mode = Minutes Mode
	 **/
	public long dateDiff(MyDate after, MyDate before, int mode) {
		
		long diff = 0;
		
		diff = after.getTime() - before.getTime();         //diff in mS
		
		if(mode == 0)					return diff / 1000;											//Seconds Mode
		else if(mode == 1)		return diff / (60 * 1000);								//Minutes Mode
		else if(mode == 2)		return diff / (60 * 60 * 1000);						//Hours Mode
		else if(mode == 3)		return diff / (24 * 60 * 60 * 1000);				//Days Mode
		else 								return diff / (60 * 1000);								//Default Mode = Minutes Mode
		
	}
	
	public boolean isEqual(MyDate date1, MyDate date2) {
		
		if((date1.toString()).equals(date2.toString())) return true;
		
		return false;
		
	}
	
	public boolean isEqualDate(MyDate date1, MyDate date2) {
		
		if((date1.getDay()).equals(date2.getDay())) return true;
		
		return false;
		
	}
	
	public boolean isBetween(MyDate start, MyDate end) {
		
		if(after(start) && before(end)) return true;
		
		return false;
		
	}
	
	public boolean before(MyDate before) {
		
		if(date.before(before.getDate())) return true;
		
		return false;
		
	}
	
	public boolean after(MyDate after) {
		
		if(date.after(after.getDate())) return true;
		
		return false;
		
	}
	
	public void print() {
		
		System.out.println(this.toString());
		
	}
	
	public String toString() {
		
		return sdf.format(date);
		
	}

	public static String getDateformat() {
		return DateFormat;
	}

	public Date getDate() {
		return date;
	}
	
	public String  getDay() {
		
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		
		return year + "-" + month + "-" + day;
		
	}
	
	public long getTime() {
		return date.getTime();
	}
	
}
