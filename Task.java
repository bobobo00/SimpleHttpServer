package V1;

import V1.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName Task
 * @Description TODO
 * @Auther danni
 * @Date 2020/1/1 18:11]
 * @Version 1.0
 **/

public  class Task implements Runnable{
    private Socket socket;
    Task(Socket socket){
        this.socket=socket;
    }

    public void run(){
        try{
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            //解析请求
            Request request=Request.parse(is);
            System.out.println(request);
            //处理业务
            if(request.path.equalsIgnoreCase("/")){
                String body="<h1>成功</h1>";
                byte[] bodyBuff=body.getBytes();
                StringBuilder response=new StringBuilder();
                response.append("HTTP/1.0 200 OK\r\n");
                response.append("Content-Type: text/html;charset=UTF-8\r\n");
                response.append("Content-Length:");
                response.append(bodyBuff.length);
                response.append("\r\n");
                response.append("\r\n");
                os.write(response.toString().getBytes("UTF-8"));
                os.write(bodyBuff);
                os.flush();
            }else{
                StringBuilder response=new StringBuilder();
                String body="<h1>404</h1>";
                byte[] bodyBuff=body.getBytes();
                response.append("HTTP/1.0 404 Not Found\r\n");
                response.append("\r\n");
                os.write(response.toString().getBytes("UTF-8"));
                os.write(bodyBuff);
                os.flush();
            }
            socket.close();
        }catch(IOException e){
            System.err.println("服务器异常！");
        }
    }
}
