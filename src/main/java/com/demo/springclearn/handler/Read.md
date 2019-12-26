案例思路：

1. 创建一个handler对象，实现BeanDefinitionRegistryPostProcessor接口，在spring容器创建时，会去刷后置处理方法

2. 在后置处理方法中生成代理对象，并封装为BeanDefinition对象，注入到spring容器中

3. 使用扫描类扫描指定路径下资源（加上自定义注解），获得资源开始封装对象


借鉴：spring中去扫描@Component的思路

具体过程参照：AnnotationConfigApplicationContext的scan方法



