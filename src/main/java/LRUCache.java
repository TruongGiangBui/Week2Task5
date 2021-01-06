import java.util.Iterator;
import java.util.LinkedHashMap;

public class LRUCache<K,V> {
    private int capacity;
    private LinkedHashMap<K,V> cache;
    public LRUCache(int capacity){
        this.capacity=capacity;
        this.cache=new LinkedHashMap<K,V>();
    }
    public V get(K key)
    {
        V value=this.cache.get(key);
        if(value!=null) {
            this.set(key, value);
        }
        return value;
    }
    public void set(K key,V value)
    {
        if(this.cache.containsKey(key))
        {
            this.cache.remove(key);
        }else if(this.cache.size()==this.capacity)
        {
            Iterator<K> iterator=this.cache.keySet().iterator();
            iterator.next();
            iterator.remove();
        }
        this.cache.put(key,value);
    }
    public void clear()
    {
        this.cache.clear();
    }
    public synchronized boolean isempty()
    {
        return this.cache.size()==0;
    }
}
