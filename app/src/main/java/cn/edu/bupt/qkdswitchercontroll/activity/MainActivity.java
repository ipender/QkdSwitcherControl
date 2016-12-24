package cn.edu.bupt.qkdswitchercontroll.activity;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.topeet.serialtest.Com;

import java.io.IOException;

import cn.edu.bupt.qkdswitchercontroll.R;
import cn.edu.bupt.qkdswitchercontroll.constant.ServerConstant;
import cn.edu.bupt.qkdswitchercontroll.device.USB2SerialProxy;
import cn.edu.bupt.qkdswitchercontroll.device.WSSSetup;
import cn.edu.bupt.qkdswitchercontroll.httpServer.HttpServer;
import cn.edu.bupt.qkdswitchercontroll.socketServer.WSSControlThread;
import cn.edu.bupt.qkdswitchercontroll.wssconstant.WSSCmd;

public class MainActivity extends Activity {

    SurfaceView sView;
    SurfaceHolder sfHolder;

    //for test by pd
    EditText uartRcvView;
    Button uart_send_btn;
    //for test by pd

    WSSControlThread controlThread = new WSSControlThread();
    Button WSS_RES;
    HttpServer server = new HttpServer(ServerConstant.SERVER_PORT);
    private Paint paint;
    final int HEIGHT = 320;
    final int WIDTH = 320;
    final int X_OFFSET = 5;

    int centerY = HEIGHT / 2;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // 启动WSS配置线程，并等待server接收到消息
        new Thread(controlThread).start();
        Log.i("Main", "socket start");

        try { // 开启Server服务器
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sView = (SurfaceView) findViewById(R.id.surface);
        sfHolder = sView.getHolder();
        WSS_RES = (Button) findViewById(R.id.btnRes);

        //
        // DisplayMetrics metric = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(metric);
        // int width = metric.widthPixels; // ��Ļ��ȣ����أ�
        // int height = metric.heightPixels; // ��Ļ�߶ȣ����أ�

        // sfHolder.addCallback(new MySurfaceView(sfHolder, width,
        // height,this));
        // ViewConstant.setScreenHeight(height);
        // ViewConstant.setScreenWidth(width);
        // setContentView(new MySurfaceView(this));

        WSS_RES.setOnClickListener(new BtnOnClick());

        /****for test by pd************/
        uartRcvView = (EditText) findViewById(R.id.uart_view);
        uartRcvView.setText("Uart Test: \n");
        uart_send_btn = (Button) findViewById(R.id.send_uart_btn);
        final USB2SerialProxy uartProxy = new USB2SerialProxy(getApplicationContext());
        USB2SerialProxy.FtdiUartRead uartRead = new USB2SerialProxy.FtdiUartRead() {
            @Override
            public void readWhenUartCallback(byte[] readData, int length) {
                uartRcvView.append(new String(readData) + "\n");
            }
        };
        uartProxy.registerForUartRead(USB2SerialProxy.PORT.A, uartRead);
        uart_send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = uartRcvView.getText().toString();
                text = text.substring(text.lastIndexOf("\n"));
                uartProxy.sendMessage(USB2SerialProxy.PORT.A, text.getBytes());
            }
        });
        /****for test by pd************/
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (server != null && server.isAlive()) {
            server.stop();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        System.exit(0);
    }

    class BtnOnClick implements View.OnClickListener {

        private static final String TAG = "BTNONCLICK";

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.btnRes) {
                Log.i(TAG, "WSS 复位");
                String res1 = WSSSetup.getInstance().sendAndGetResponse(
                        controlThread.SWSS1, Com.WSS1, WSSCmd.COMMEND_RES);
                String res2 = WSSSetup.getInstance().sendAndGetResponse(
                        controlThread.SWSS2, Com.WSS2, WSSCmd.COMMEND_RES);
                if (res1 != null && res2 != null)
                    if (res1.contains("OK") && res2.contains("OK")) {
                        Log.i(TAG, "复位成功");
                        Toast.makeText(MainActivity.this, "复位成功",
                                Toast.LENGTH_LONG).show();
                    }
            }
        }

    }
}
