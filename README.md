# AIDLSimple
此处Simple，包含了怎么AIDL进行IPC跨进程通信的简单使用及在使用时需要注意的地方，具体实现步骤如下
#### 1,在AndroidStudio中右键，新建一个名为RemoteServer的AIDL文件
创建完成后里面会默认生成一个basicTypes的方法，可以根据自己需要删除或者修改
#### 2，在文件中添加一个简单的接口，并重新构建项目
  例如：String getResult（）;等简单的接口，
  
  interface RemoteServer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getResultMsg();
}
  
#### 3，创建一个RemoteService的Service类
在RemoteService中通过RemoteServer.Stub() 获得Binder对象

public class RemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    RemoteServer.Stub iBinder=new RemoteServer.Stub() {
        @Override
        public String getResultMsg() throws RemoteException {
            return "getResult msg"+ Process.myPid()+Process.myUid();
        }
    };
}

#### 4，在配置文件中添加该Service的注册
在使用时一定要添加enable和exported为true的属性 这里通过设置Service为独立的remote进程测试使用，所以添加process属性
        <service android:name=".RemoteService"
            android:process=":remote"
            android:enabled="true"
            android:exported="true"/>
            
#### 5，在Activity中绑定Service进行跨进程通信
public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    RemoteServer remoteServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    private void bindService(){
        Intent intent=new Intent(this,RemoteService.class);
        bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    private void unbindService(){
        if (serviceConnection!=null)unbindService(serviceConnection);
    }

    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteServer= RemoteServer.Stub.asInterface(service);
            try {
                //这里获得跨进程的消息
                String processMsg=remoteServer.getResultMsg();
                Log.i(TAG,"get result:"+processMsg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteServer=null;
        }
    };
}

这里只是简单实现，在开发中，如果跨两个应用进行通信，则需要把aidl文件Copy到另外一个项目中进行Service的实现
需要注意的是

1，在传递对象时，需要通过Parcelable进行序列化，例如如下City
import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
    /**
     * 城市名
     */
    private String name;
    /**
     * 城市面积
     */
    private int areaSize;

    protected City(Parcel in) {
        name = in.readString();
        areaSize = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(int areaSize) {
        this.areaSize = areaSize;
    }


    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(areaSize);
    }
    //此方法不会自动生成，需要自己添加，否则编译会报错
    public void readToParcel(Parcel parcel){
        name=parcel.readString();
        areaSize=parcel.readInt();
    }

    @Override
    public String toString() {
        return "{ name:"+name+",areaSize:"+areaSize+"}";
    }
}

2，创建一个名为实体类一样的aidl文件，删除里面的方法添加parcelable 加实体类名，如下：
parcelable City;

3，创建一个管理类的aidl文件，添加方法，并倒入该实体类，在添加方法时，注意aidl接口的规则需要在参数前，添加 in，out，intout三个标示符号，代表的是流的方向

in ，代表，客户端流向服务端，表示该对象是从客户端传向服务端，在服务端修改这个对象不会对客户端输入的对象产生影响

out，服务端流向客户端，数据只能从服务端影响客户端，即客户端输入这个参数时，服务端并不能获取到客户端的具体实例中的数据，而是生成一个默认数据，
但是服务端对这个默认数据的修改会影响到客户端的这个类对象实例发生相应的改变

inout，则是双向的
