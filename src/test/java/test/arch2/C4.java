package test.arch2;

class C4 {

	public void doSthg() {

		doSthgInternal();
		
		C5 c5 = new C5();
		c5.doSthg();
	}
	
	public void doSthgElse() {
		doSthgElseInternal();
	}
	
	private void doSthgInternal() {
		System.out.println("C4::doSthg");
	}
	
	private void doSthgElseInternal() {
		System.out.println("C4::sthgElse");
	}
}
