package top.xmdhs.skin;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.SerializedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import cn.nukkit.Player;

public class main extends PluginBase{

    @Override
    public void onLoad() {
      this.getLogger().info("加载成功");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //这里返回true代表执行成功，false则不成功
        //sender 是发送命令的对象
        //command 命令对象
        // [1]
        //label 是标签，如果注册命令时label为null，就会默认为命令名，label的组成是fallBackPrefix+:+label
        //label这个地方其实也不是很懂。一般注册时都是""或者null
        //通过源码推测label和fallBackPrefix的组合是命令的唯一标识
        //
        //args 命令参数,比如/hello 1 2的参数为1和2,存储在数组中
        //这里使用命令通过equals
        //如何得到指令名称个人习惯原因
        if("skin".equals(command.getName())){
            if(sender.isPlayer()) {
                Player player = (Player) sender;
                String man = sender.getName();
                sender.sendMessage("大概提取成功了吧");
                SerializedImage sskin = player.getSkin().getSkinData();
                setData(sskin,man);
            }else {
               if (args.length > 0) {
                 try {
                     String mans = args[0];
                     Player player = Server.getInstance().getPlayer(mans);
                     SerializedImage sskin = player.getSkin().getSkinData();
                     setData(sskin, mans);
                     getLogger().info("我觉得成功了");
                 } catch (Exception e) {
                     getLogger().info("玩家大概不存在");
                 }
               }else {
                   getLogger().info("/skin <玩家名> ，获取玩家的皮肤，皮肤储存于服务器根目录下的 skins 文件夹");
               }
        }
            return true;//最好加上
        }
        //这种方式虽然方便，但是命令多，且命令功能复杂时会难以维护
        //少部分命令可以使用它
        return true;
    }
    public void setData(SerializedImage data, String player) {
        int width = 0;
        int height = 0;
        width = data.width;
        height = data.height;
        DataBuffer buffer = new DataBufferByte(data.data, data.data.length);
        WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 4 * width, 4, new  int[] {0, 1, 2, 3}, (Point)null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(cm, raster, true, null);
        String dateStr = Long.toString(System.currentTimeMillis()/1000L);
        try {
            File dir = new File("skins");
          if (!dir.exists()) {
              dir.mkdir();
          }
            ImageIO.write(image, "png", new File(dir, player + "-" + dateStr + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
