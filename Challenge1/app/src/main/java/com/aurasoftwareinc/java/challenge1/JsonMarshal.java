package com.aurasoftwareinc.java.challenge1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JsonMarshal
{
    public static JSONObject marshalJSON(Object object)
    {
        //
        // Todo: replace contents of this method with Your code.
        //




        JSONObject json = new JSONObject();
        JSONObject SubclassTypesJson = new JSONObject();


        try
        {


            //Get class of object
            Class myObjectClass = object.getClass();

            //Get Fields of myObjectClass
            Field primitiveTypesObjectField = myObjectClass.getDeclaredField("primitiveTypes");
            Field objectTypesObjectField = myObjectClass.getDeclaredField("objectTypes");
            Field jsonTypesObjectField = myObjectClass.getDeclaredField("jsonTypes");




            //Get class of fields
            Class primitiveTypesObjectClass = primitiveTypesObjectField.getType();
            Class objectTypesObjectClass = objectTypesObjectField.getType();
            Class jsonTypesObjectClass = jsonTypesObjectField.getType();





            for (Field field : myObjectClass.getDeclaredFields()) {

                //Before proceeding, check whether variables in 'SubclassTypes' class are private or not. (Ex: objectTypes is private)
                //If so, we must make them Accessible first in order to read the data they contain
                if(Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }

                //Will hold the classname of field that is currently being analyzed by Reflection
                Class<?> fieldType = field.getType();




                if(fieldType == primitiveTypesObjectClass){


                    /*** PrimitiveTypes.class MARSHALLING ***/

                    JSONObject primitiveTypesJson = new JSONObject();

                    for (Field fieldVariable : fieldType.getDeclaredFields()) {



                        /*if (fieldVariable.getType().isAssignableFrom(Byte.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Short.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Integer.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Integer.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Long.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Float.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Double.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Boolean.TYPE)
                                || fieldVariable.getType().isAssignableFrom(Byte.class)

                                && !Modifier.isStatic(fieldVariable.getModifiers())
                                && !fieldVariable.getName().equals("serialVersionUID"))  {*/


                        if (true && !fieldVariable.getName().equals("serialVersionUID"))  {


                            //If field is private, then we first must set Accessible to true in order to retrieve it that hidden value :)
                            if(Modifier.isPrivate(fieldVariable.getModifiers())) {
                                fieldVariable.setAccessible(true);
                            }


                            if(fieldVariable.getType().isArray()){

                                JSONArray jsonArray = new JSONArray();
                                Object arrayObject = fieldVariable.get(field.get(object));

                                int length = Array.getLength(arrayObject);
                                for (int i = 0; i < length; i ++) {
                                    Object arrayElement = Array.get(arrayObject, i);
                                    jsonArray.put(arrayElement);
                                }

                                primitiveTypesJson.put(fieldVariable.getName(),jsonArray);

                                }else{
                                primitiveTypesJson.put(fieldVariable.getName(), fieldVariable.get(field.get(object)));

                            }

                            //Field primShortField = fieldType.getDeclaredField("primShort");

                        }

                    }

                    SubclassTypesJson.put(field.getName(), primitiveTypesJson);




                        /***WORKS!***/
                    /*
                    Field primShortField = fieldType.getDeclaredField("primShort");


                    //If field is private, then we first must set Accessible to true in order to retrieve it that hidden value :)
                    if(Modifier.isPrivate(primShortField.getModifiers())) {
                        primShortField.setAccessible(true);
                    }
                    short primShortValue = (Short) primShortField.get((PrimitiveTypes)field.get(object));*/




                }else if(fieldType == objectTypesObjectClass){


                    /*** ObjectTypes.class MARSHALLING ***/

                    JSONObject objectTypesJson = new JSONObject();

                    for (Field fieldVariable : fieldType.getDeclaredFields()) {




                        if (true && !fieldVariable.getName().equals("serialVersionUID"))  {


                            //If field is private, then we first must set Accessible to true in order to retrieve it that hidden value :)
                            if(Modifier.isPrivate(fieldVariable.getModifiers())) {
                                fieldVariable.setAccessible(true);
                            }





                            if(fieldVariable.getType().isArray()){

                                JSONArray jsonArray = new JSONArray();
                                Object arrayObject = fieldVariable.get(field.get(object));


                                //Make sure to check whether arrayObject is null (Otherwise an exception occurs)
                                if(arrayObject==null){

                                    //jsonArray.put(JSONObject.NULL);
                                    objectTypesJson.put(fieldVariable.getName(), JSONObject.NULL);

                                }else{

                                    //Will traverse each element inside Array and add them to a JsonArray
                                    int length = Array.getLength(arrayObject);
                                    for (int i = 0; i < length; i ++) {
                                        Object arrayElement = Array.get(arrayObject, i);
                                        jsonArray.put(arrayElement);
                                    }

                                    objectTypesJson.put(fieldVariable.getName(), jsonArray);

                                }


                            }else{

                                //Check if its null
                                if(fieldVariable.get(field.get(object))==null){
                                    objectTypesJson.put(fieldVariable.getName(), JSONObject.NULL);

                                }else{
                                    objectTypesJson.put(fieldVariable.getName(), fieldVariable.get(field.get(object)));

                                }


                            }




                            //Field primShortField = fieldType.getDeclaredField("primShort");

                        }

                    }

                    SubclassTypesJson.put(field.getName(), objectTypesJson);






                }else if(fieldType == jsonTypesObjectClass){


                    /*** JSONTypes.class MARSHALLING ***/

                    JSONObject jsonTypesJson = new JSONObject();


                    for (Field fieldVariable : fieldType.getDeclaredFields()) {

                        if(fieldVariable.getType() == JSONObject.class){


                            jsonTypesJson.put(fieldVariable.getName(), fieldVariable.get(field.get(object)));


                        }else if(fieldVariable.getType() == JSONArray.class){


                            //If field is private, then we first must set Accessible to true in order to retrieve it that hidden value :)
                            if(Modifier.isPrivate(fieldVariable.getModifiers())) {
                                fieldVariable.setAccessible(true);
                            }

                            jsonTypesJson.put(fieldVariable.getName(), fieldVariable.get(field.get(object)) );


                        }





                    }

                    SubclassTypesJson.put(field.getName(), jsonTypesJson);


                }


            }




            //Merge all Jsons into 1
            json.put(myObjectClass.getSimpleName(), SubclassTypesJson);



        }
        catch (Exception ignore)
        {


        }

        return json;
    }








    public static boolean unmarshalJSON(Object object, JSONObject json)
    {
        //
        // Todo: replace contents of this method with Your code.
        //


        //Get class of object
        Class myObjectClass = object.getClass();
        String nameOfClass = myObjectClass.getSimpleName();



        try {


            for (Field field : myObjectClass.getDeclaredFields()) {


                //Will hold the classname of field that is currently being analyzed by Reflection
                //Class<?> fieldType = field.getType();


                /***IMPORTANT ISSUE!!!**/
                //  !isSynthetic() issue : Android Instant run implements an interface name "runtime.IncrementalChange". When we call 'getDeclaredFields',  a weird field name "$change"
                //                         shows itself out of nowhere, which happens to come from Android Instant run.
                //                         To skip this field, we implement !isSynthetic(), which will ignore any fields generated by Android Instant run, in this case "$change"
                if (!field.getName().equals("serialVersionUID") &&  !field.isSynthetic()) {

                    Class primitiveTypesObjectClass = field.getType();
                    Class<?> referenceT  =   field.getClass();

                    String fieldName = field.getName();



                    if(field.getType().isPrimitive()){

                        field.setAccessible(true);

                        //For primitives, we can set the value directly. But lets not forget the null check :)
                        if(json.get(field.getName()) == JSONObject.NULL){
                            field.set(object, null);

                        }else{
                            field.set(object, json.get(field.getName()));

                        }



                    }else{


                        Class<?> reference  =   field.getType();
                        //Since the local variables of Object are empty(null), we must create an instance of each variable in order to proceed



                        //IMPORTANT!!!  We must check whether obj is an Instance of JsonMarshallInterface.
                        //CASE 1: If it is, it means current object implements JsonMarshallInterface contract, meaning it can be unmarshalled even further
                        //CASE 2: If it isnt, then it means we reached end of unmarshmall for current object
                        if(JsonMarshalInterface.class.isAssignableFrom(reference)){





                            Object obj =reference.newInstance();

                            JSONObject jsonObject =  json.getJSONObject(nameOfClass);
                            JSONObject jsonTypesResponseData = null;

                            jsonTypesResponseData = jsonObject.getJSONObject(fieldName);


                            //Lets make variable accessible, just in case its private :)
                            field.setAccessible(true);

                            //The value of the variable is null initially. We set a value to the variable by setting our previously created object to it
                            field.set(object, obj);


                            Method unmarshalJSONMethod = reference.getDeclaredMethod("unmarshalJSON",  JSONObject.class);

                            // invokes the method at runtime
                            unmarshalJSONMethod.invoke(obj, jsonTypesResponseData);


                            //((ObjectTypes) obj).unmarshalJSON(jsonTypesResponseData);


                        }else{


                            //Lets make variable accessible, just in case its private :)
                            field.setAccessible(true);


                            //If the field is an array, then we need to traverse the array and add each value individually
                            //Otherwise we can set the value directly
                            if(field.getType().isArray()){

                                if(json.isNull( field.getName()) ){
                                    field.set(object, null);

                                }else{

                                    int lengthOfArray= json.getJSONArray(field.getName()).length();

                                    // First, create the array
                                    Object myArray = Array.newInstance(field.getType().getComponentType(), lengthOfArray);

                                    // Then, adding value to that array
                                    for (int i = 0; i < lengthOfArray; i++) {

                                        Array.set(myArray, i, json.getJSONArray(field.getName()).get(i));
                                    }


                                    // Finally, set value for that array field
                                    field.set(object, myArray);

                                }





                                //The value of the variable is null initially. We set a value to the variable by setting our previously created object to it
                                //field.set(object, json.getJSONArray(field.getName()) );




                            }else{

                                //The value of the variable is null initially. We set a value to the variable by setting our previously created object to it
                                if(json.isNull( field.getName()) ){
                                    field.set(object, null);

                                }else{
                                    field.set(object, json.get(field.getName()) );

                                }



                            }



                        }


                    }


                }



            }


        } catch (JSONException e) {

            e.printStackTrace();


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return true;


    }
}