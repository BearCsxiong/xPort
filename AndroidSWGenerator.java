import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @Desc : SW最小适配方案
 * ldpi——120dpi—-1dp=0.75px
 * mdpi—-160dpi—-1dp=1px
 * hdpi——240dpi—-1dp=1.5px
 * xhdpi—-320dpi—-1dp=2px
 * xxhdpi—480dpi—-1dp=3px
 * @Author : csxiong create on 2019/7/17
 */
public class AndroidSWGenerator {
    // 生成最大dp内容
    static int MAX_DP = 1000;
    // 生成最小dp内容
    static int MIN_DP = -1000;
    //生成负数dimen前缀
    static String MINUS_SHUFFIX_DES_NAME = "m_x";
    // 生成dimen前缀名称
    static String PRE_SHUFFIX_DES_NAME = "x";
    // 基准dp,以像素密度为基准比例为的是和UI标注图中px相当
    // 例如:
    // 1.UI设计效果图设计像素密度为160PPI
    // 2.标注图中1px=1dp
    // 3.我们生成一套以160为基准的比例文件
    // 4.按UI标注图中PX使用我们生成的标注文件就OK了
    //-----------------------------------------------
    // 如果一下例子 360/160 ~ 2.25
    // 我们生成的dp方案就要2.25px=1dp生成最小宽度文件
    // 这样才能适应设计图比例
    static float BASE_DP = 360.0f;
    // 生成根路径,使用则修改为你想要生成的位置根路径
    static String GENATATOR_PATH = "/Users/csxiong/Desktop/Android适配基于" + ((int) BASE_DP) + "像素密度dp-px";

    public static void main(String[] args) {
        //最小宽度的范围 因碎片化严重 密度越高 适配效果越好
        int[] j = {120, 160, 240, 320, 360, 420, 480, 500, 520, 560, 600};

        for (int m = 0; m < j.length; m++) {
            System.out.println(" \n适配大小" + j[m] + "\n");
            createXML(j[m]);
        }
    }

    private static void createXML(int position) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("resources");
            for (int i = MIN_DP; i <= MAX_DP; i++) {
                Element dimen = document.createElement("dimen");
                dimen.setAttribute("name", (i < 0 ? (MINUS_SHUFFIX_DES_NAME + (-i)) : (PRE_SHUFFIX_DES_NAME + i)));
                dimen.setTextContent(new BigDecimal(i * (position / BASE_DP)).setScale(2, RoundingMode.HALF_UP) + "dp");
                rootElement.appendChild(dimen);
            }

            document.appendChild(rootElement);
            TransformerFactory tff = TransformerFactory.newInstance();

            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            File file = new File(GENATATOR_PATH + "/values-sw" + position + "dp" + "/dimens.xml");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            } else if (file.isFile() && !file.exists()) {
                file.createNewFile();
            }
            tf.transform(new DOMSource(document), new StreamResult(file));
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

