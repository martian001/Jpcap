package test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Vector;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class NetTest {
   public static void main(String[] args) throws IOException {
      new NetTest().start();
   }

   public void start() throws IOException {
      JpcapCaptor jpcap = getJpcap();// 加载网卡
      while (true) {
         jpcap.processPacket(1, handler);
      }

   }

   private PacketReceiver handler = new PacketReceiver() {
      // 抓包监听器
      @Override
      public void receivePacket(Packet packet) {
         dealPacket(packet);
      }

   };

   public JpcapCaptor getJpcap() throws IOException {
      // new Jcapturedialog(parent).setVisible(true);
      // 加载网卡
      NetworkInterface[] devices;
      devices = JpcapCaptor.getDeviceList();
      if (devices == null) {
         System.out.println("没有找到网卡");
         return null;
      } else {
         String[] names = new String[devices.length];
         for (int i = 0; i < names.length; i++) {
            names[i] = (devices[i].description != null ? devices[i].name : devices[i].description);
         }
      }
      // caplen:捕获长度 1整个数据包=1514 3.仅首部68
      JpcapCaptor jpcap = JpcapCaptor.openDevice(devices[1], /*caplen*/68, /*是否设置为混杂模式*/true, 50);
      // 是否使用使用过滤
      if (1 == 1) {
         // jpcap.setFilter("ip.dst==116.7.226.76", true);
      }
      return jpcap;
   }

   // 打印包数据
   public void dealPacket(Packet packet) {
      try {
         Vector r = new Vector();
         String strtmp;
         Timestamp timestamp = new Timestamp((packet.sec * 1000) + (packet.usec / 1000));

         r.addElement(timestamp.toString()); // 数据报时间
         r.addElement(((IPPacket) packet).src_ip.toString()); // 源IP地址
         r.addElement(((IPPacket) packet).dst_ip.toString()); // 目的IP地址
         r.addElement(packet.header.length); // 首部长度
         r.addElement(packet.data.length); // 数据长度
         r.addElement(((IPPacket) packet).dont_frag == true ? "分段" : "不分段"); // 是否不分段
         r.addElement(((IPPacket) packet).offset); // 数据长度

         strtmp = new String(packet.header, "gb2312");
         r.addElement(strtmp); // 首部内容

         strtmp = new String(packet.data, "gb2312");
         r.addElement(strtmp); // 数据内容

         // if ("/116.7.226.76".equals(((IPPacket) packet).dst_ip.toString())) {
         System.out.println(r);
         // }
      } catch (Exception e) {

      }
   }
}
