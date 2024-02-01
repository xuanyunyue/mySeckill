package github.xuanyunyue.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker辅助组件
 */
public class FreemarkerHelper {

    private static Configuration _tplConfig = new Configuration();

    static{
        try {
            _tplConfig.setDirectoryForTemplateLoading(new File("D:\\0project\\seckill\\seckill-page\\src\\main\\java\\github\\xuanyunyue\\freemarker\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析freemarker模板
     * @param tplName
     * @param encoding
     * @param paras
     * @return
     */
    public String parseTemplate(String tplName, String encoding, Map<String, Object> paras) {
        try {
            StringWriter swriter = new StringWriter();
            Template mytpl = null;
            mytpl = _tplConfig.getTemplate(tplName, encoding);
            mytpl.process(paras, swriter);
            return swriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String parseTemplate(String tplName, Map<String, Object> paras) {
        return this.parseTemplate(tplName, "utf-8", paras);
    }

}