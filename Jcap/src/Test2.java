public class Test2 {
    public void test2() {
        Object object = new Object();
        object = null;
        if (1==2) {
            object=new Object();
            
        }else{
            object=new Object();
            
        }
        object.notifyAll();

    }
}
