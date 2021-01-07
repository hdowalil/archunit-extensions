package test.arch2;

public class C1 {

	public void doSthg() {
		
		System.out.println("C1::doSthg");
		
		C2 c2 = new C2();
		c2.doSthg();
		
		C3 c3 = new C3();
		c3.doSthg();
		
		C4 c4 = new C4();
		c4.doSthg();
		
	}
}
