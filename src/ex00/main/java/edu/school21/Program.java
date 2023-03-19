package edu.school21;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Program {

    private static final int index =  "edu.school21.classes.".length();
    private static Object object;
    private static Object[] objects;

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        var classes = getList();
        objects = new Object[classes.size()];

        for (int i = 0; i < classes.size(); ++i) {
            objects[i] = classes.get(i).getDeclaredConstructor().newInstance();
        }

        System.out.println("Classes:");
        for (Class<?> a : classes) {
            System.out.println("- " + a.getName().substring(index));
        }
        System.out.println("_________________");
        System.out.println("Enter class name:");

        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        for (Class<?> a : classes) {
            if (a.getName().substring(index).equals(str)) {

                System.out.println("fields");
                Field[] field = a.getDeclaredFields();
                for (Field f : field) {
                    System.out.println("     " + f.getAnnotatedType() + " " + f.getName());
                }

                System.out.println("methods");
                Method[] methods = a.getDeclaredMethods();
                for (Method m : methods) {
                    System.out.println("     " + m.getName());
                }

                System.out.println("_________________");
                System.out.println("Let's create an object.");

                for (Object ob : objects) {
                    if (ob.getClass().getSimpleName().equals(str)) {
                        object = ob;
                        for (Field f : object.getClass().getDeclaredFields()) {
                            System.out.println(f.getName() + ":");
                            f.setAccessible(true);
                            f.set(object, getInfoFromString(f.getType().getSimpleName()));
                            f.setAccessible(false);
                        }
                    }
                }

                System.out.println("Object created: ");
                System.out.println(object);

                System.out.println("_________________");
                System.out.println("Enter name of the field for changing:");

                Field ff = null;
                str = scan.nextLine();
                for (Field f : field) {
                    if (f.getName().equals(str)) {
                        ff = f;
                        break;
                    }
                }
                System.out.println("Enter " + ff.getType() + " value");
                ff.setAccessible(true);
                ff.set(object, getInfoFromString(ff.getType().getSimpleName()));
                ff.setAccessible(false);
                System.out.println("Object updated: " + object);

                System.out.println("_________________");
                System.out.println("Enter name of the method for call:");

                Method method = null;
                str = scan.nextLine();

                for (Method m : methods) {
                    if (m.getName().equals(str)) {
                        method = m;
                        break;
                    }
                }

                List<Object> parameters = new LinkedList<>();
                Parameter [] parameters1 = method.getParameters();

                for (Parameter p : parameters1) {
                    System.out.println("Enter " + p.getType().getSimpleName() + " value:");
                    parameters.add(getInfoFromString(p.getType().getSimpleName()));
                }

                Object returnValue =  method.invoke(object, parameters.toArray());

                if (!method.getReturnType().getSimpleName().equals("void")) {
                    System.out.println("Method returned:");
                    System.out.println(returnValue);
                }
                System.out.println(object);
            }
        }
    }

    private static List<Class<?>> getList() throws ClassNotFoundException {
        Package pack = Program.class.getPackage();
        String name = pack.getName().replaceAll("\\.", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        File dir = new File(url.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();

        for (File files : dir.listFiles()) {
            if (files.isDirectory()) {
                for (File file : files.listFiles()) {
                    String tmpName = file.toString().replaceAll("/", "\\.");
                    tmpName = tmpName.substring(tmpName.indexOf(pack.getName()));
                    String className = tmpName.substring(0, tmpName.lastIndexOf(".class"));
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    public static Object getInfoFromString(String raw) {
        Scanner scanner = new Scanner(System.in);
        switch (raw.toLowerCase()) {
            case "string":
                return scanner.nextLine().trim();
            case "int":
            case "integer":
                return scanner.nextInt();
            case "long":
                return scanner.nextLong();
            case "boolean":
                return scanner.nextBoolean();
            case "double":
                return scanner.nextDouble();
            case "float":
                return scanner.nextFloat();
            case "char":
            case "character":
                return scanner.nextLine().charAt(0);
        }
        return null;
    }

}
