/** 
 * Project Name:spark-java 
 * File Name:CounterMap.java 
 * Date:2017年12月8日上午11:24:17 
 * 
* 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃ 　
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　... ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃   神兽无影，BUG无踪！
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃  　　　　　　
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃　　　　　　　┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
*
*
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽无影，BUG无踪！
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 *
 * ━━━━━━感觉萌萌哒━━━━━━
*/ 
package com.seed.streaming;
/** 
 * ClassName:CounterMap
 * Date:     2017年12月8日 上午11:24:17 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CounterMap {
  HashMap<String, Counter> map = new HashMap<String, Counter>();
  
  public void increment(String key, long increment) {
	  Counter count = map.get(key);
    if (count == null) {
      count = new Counter();
      map.put(key, count);
    } 
//    count.value += increment;
    count.setValue(count.getValue() + increment);
  }
  
  
  public long getValue(String key) {
    Counter count = map.get(key);
    if (count != null) {
      return count.getValue();
    } else {
      return 0;
    }
  }
  
  public Set<Entry<String, Counter>> entrySet() {
    return map.entrySet();
  }
  
  public void clear() {
    map.clear();
  }
  
  public static class Counter {
    public long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
    
  }
  
  
}
