package com.varsql.web.common.constants;

public interface ResultConstants {
	
	public String MESSAGE ="msg";
	public String CODE ="code";
	public String RESULT_ITEMS ="items";
	
	enum CODE_VAL{
		SUCCESS(200), ERROR(500), NOT_FOUND(400);
		
		int code = -1; 
		CODE_VAL(int pcode){
			this.code = pcode;
		}
		
		@Override
		public String toString() {
			return this.code+"";
		}
	}
}
