import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;

public class Task5 {
    private static final Logger logger
            = LoggerFactory.getLogger(Task5.class);
    public static void main(String[] args) {

        NumberServices numberServices=new NumberServices(){
            @Override
            public ArrayList<Integer> getListNumber(int n)
            {
                ArrayList<Integer> list=new ArrayList<>();
                ArrayList<Boolean> primecheck=new ArrayList<>(n+1);
                for(int i=0;i<=n;i++)
                {
                    primecheck.add(true);
                }
                for(int i=2;i<=n;i++)
                {
                    if(primecheck.get(i)) {
                        list.add(i);
                        int j=i;
                        while(j<=n)
                        {
                            primecheck.set(j,false);
                            j+=i;
                        }
                    }
                }
                return list;
            }
        };

        LoadingCache<Integer,ArrayList<Integer>> cache=
                CacheBuilder.newBuilder()
                .maximumSize(10).expireAfterWrite(20,TimeUnit.SECONDS)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> load(Integer integer) throws Exception {
                        return numberServices.getListNumber(integer);
                    }
                });

        get("/prime/:n",((request, response) -> {
            response.type("application/json");
            Integer param=Integer.parseInt(request.params(":n"));
            ArrayList<Integer> result=cache.get(param);
            logger.info("request at {}",System.currentTimeMillis()%1000000);
//            System.out.println(cache.asMap().toString());
//            CacheStats cacheStats=cache.stats();
//            System.out.println(cacheStats.toString());
//            System.out.println("===============");
            return new Gson().toJsonTree(result);
        }));
    }
}
