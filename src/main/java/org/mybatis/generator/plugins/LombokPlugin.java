package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author admin
 */
public class LombokPlugin extends PluginAdapter {

    private static final String IGNORE_FIELDS = "ignoreFields";

    private static final String MY_SUPPER_CLASS = "supperClass";

    private static final String AUTHOR = "author";

    private static final String DAO_ANNOTATION = "daoAnnotation";

    private String supperClass;

    public LombokPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> list) {
        supperClass = properties.getProperty(MY_SUPPER_CLASS);
        return true;
    }

    protected String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return dateFormat.format(new Date());
    }

    private String getIgnoreFields() {
        return properties.getProperty(IGNORE_FIELDS);
    }

    private String getAuthor() {
        return properties.getProperty(AUTHOR);
    }

    private String getDaoAnnotation() {
        return properties.getProperty(DAO_ANNOTATION);
    }

    private static String camelToUnderline(String fieldName) {
        StringBuilder result = new StringBuilder();
        if (fieldName != null && fieldName.length() > 0) {
            // 将第一个字符处理成大写
            result.append(fieldName.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < fieldName.length(); i++) {
                String s = fieldName.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");
        topLevelClass.addImportedType("lombok.EqualsAndHashCode");
        // 添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = false)");
        // 添加domain的注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * @author " + getAuthor());
        topLevelClass.addJavaDocLine(" * @date " + this.getDateString());
        topLevelClass.addJavaDocLine(" */");
        // 设置父类
        if (supperClass != null) {
            topLevelClass.setSuperClass(new FullyQualifiedJavaType(supperClass));
        }
        // 将需要忽略生成的属性过滤掉
        List<Field> fields = topLevelClass.getFields();
        String ignoreFields = getIgnoreFields();
        if (null != ignoreFields && !"".equals(ignoreFields)) {
            String[] field = ignoreFields.split(",");
            Iterator<Field> iterator = fields.iterator();
            while (iterator.hasNext()) {
                Field tableField = iterator.next();
                for (String ignoreField : field) {
                    if (ignoreField.equalsIgnoreCase(tableField.getName())) {
                        iterator.remove();
                    }
                }
            }
        }
        // 给每个字段加注释
        for (Field field : fields) {
            StringBuilder fieldSb = new StringBuilder();
            field.addJavaDocLine("/**");
            fieldSb.append(" * ");
            String fieldName = field.getName();
            String underlineFieldName = camelToUnderline(fieldName);
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(underlineFieldName);
            if (null != introspectedColumn) {
                fieldSb.append(introspectedColumn.getRemarks());
            }
            field.addJavaDocLine(fieldSb.toString().replace("\n", " "));
            field.addJavaDocLine(" */");
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // dao注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * @author " + getAuthor());
        interfaze.addJavaDocLine(" * @date " + this.getDateString());
        interfaze.addJavaDocLine(" */");
        // dao注解
        String annotationPath = getDaoAnnotation();
        FullyQualifiedJavaType importedType = new FullyQualifiedJavaType(annotationPath);
        interfaze.addImportedType(importedType);
        String annotationName = annotationPath.substring(annotationPath.lastIndexOf(".") + 1);
        interfaze.addAnnotation("@" + annotationName);
        // 引入Optional依赖
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Optional"));
        // 默认生成方法注释
        List<Method> methods = interfaze.getMethods();
        for (Method method : methods) {
            switch (method.getName()) {
                case "deleteByPrimaryKey":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 删除");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param id 主键ID");
                    method.addJavaDocLine(" * @return 影响数据条数");
                    method.addJavaDocLine(" */");
                    break;
                case "insert":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 插入");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param record 数据实体");
                    method.addJavaDocLine(" * @return 影响数据条数");
                    method.addJavaDocLine(" */");
                    break;
                case "insertSelective":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 插入Selective");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param record 数据实体");
                    method.addJavaDocLine(" * @return 影响数据条数");
                    method.addJavaDocLine(" */");
                    break;
                case "selectByPrimaryKey":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 根据主键查询");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param id 主键ID");
                    method.addJavaDocLine(" * @return 数据实体");
                    method.addJavaDocLine(" */");
                    break;
                case "updateByPrimaryKeySelective":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 更新Selective");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param record 数据实体");
                    method.addJavaDocLine(" * @return 影响数据条数");
                    method.addJavaDocLine(" */");
                    break;
                case "updateByPrimaryKey":
                    method.addJavaDocLine("/**");
                    method.addJavaDocLine(" * 更新");
                    method.addJavaDocLine(" *");
                    method.addJavaDocLine(" * @param record 数据实体");
                    method.addJavaDocLine(" * @return 影响数据条数");
                    method.addJavaDocLine(" */");
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // SelectByPrimaryKey方法返回值设置为 Optional<?>
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType("java.util.Optional");
        String domainObjectName = getDomainObjectName(introspectedTable);
        String domainObjectPath = getDomainTargetPackage() + "." + domainObjectName;
        fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(domainObjectPath));
        method.setReturnType(fullyQualifiedJavaType);
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    private String getDomainTargetPackage() {
        return this.context.getJavaModelGeneratorConfiguration().getTargetPackage();
    }

    private String getDomainObjectName(IntrospectedTable introspectedTable) {
        return introspectedTable.getTableConfiguration().getDomainObjectName();
    }
}