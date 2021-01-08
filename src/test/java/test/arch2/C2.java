package test.arch2;

class C2 {
	
	private int invoked = 0;
	
	private String formatStringOutput() {
		return String.format("C2 invoked %d times", invoked);
	}
	
	public void doSthg() {
		
		System.out.println(formatStringOutput());
		
		C5 c5 = new C5();
		c5.doSthg();
		
		invoked++;
	}

}
