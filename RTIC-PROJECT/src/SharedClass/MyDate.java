import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	public void addTime(int add) {
		
		cal.add(Calendar.MINUTE, add);
		String output = sdf.format(cal.getTime());
		try {
			
			date = sdf.parse(output);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addDate(int day) {
		
		cal.add(Calendar.DAY_OF_MONTH, day);
		String output = sdf.format(cal.getTime());
		
		try {
			
			date = sdf.parse(output);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isEqual(MyDate checkDate) {
		
		if(date.equals(checkDate.toString())) return true;
		
		return false;
		
	}
	
	public boolean isEqualDate(MyDate date1, MyDate date2) {
		
		if((date1.getDay()).equals(date2.getDay())) return true;
		
		return false;
		
	}
	
	/**
	 * start <= MyDate <= end
	 */
	public boolean isBetween(MyDate start, MyDate end) {
		
		if((isAfter(start) && isBefore(end))) return true;
		
		return false;
		
	}
	
	/**
	 * Is MyDate before or equal checkDate
	 */
	public boolean isBefore(MyDate checkDate) {
		
		if(date.before(checkDate.getDate()) || isEqual(checkDate)) return true;
		
		return false;
		
	}
	
	/**
	 * Is MyDate after or equal checkDate
	 */
	public boolean isAfter(MyDate checkDate) {
		
		if(date.after(checkDate.getDate()) || isEqual(checkDate)) return true;
		
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
		int month = cal.get(Calendar.MONTH);     // Month in Java is 0-11
		int year = cal.get(Calendar.YEAR);
		
		return year + "-" + (month + 1) + "-" + day;
		
	}
	
	public long getTime() {
		return date.getTime();
	}
	
}
