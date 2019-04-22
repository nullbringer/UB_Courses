import java.util.Random;

/*   TLA+ Specification of Water Jugs problem:
 
FillSmallJug  == /\ small' = 3 
                 /\ big' = big

FillBigJug    == /\ big' = 5 
                 /\ small' = small

EmptySmallJug == /\ small' = 0 
                 /\ big' = big

EmptyBigJug   == /\ big' = 0 
                 /\ small' = small
                  
SmallToBig == /\ big'   = Min(big + small, 5)
              /\ small' = small - (big' - big)

BigToSmall == /\ small' = Min(big + small, 3) 
              /\ big'   = big - (small' - small)

 */

public class WaterJugs {
	
		 int big = 0;
		 int small = 0;
		 int i = 1;
		 
		 void FillSmallJug()  { small = 3; }
		 void FillBigJug()    { big = 5;}
		 void EmptySmallJug() { small = 0; }
		 void EmptyBigJug()   { big = 0; } 
		 
		 void SmallToBig()    { int t = big;
		 						if (big+small < 5) 
		 							 big = big+small; 
		 						else big = 5; 
		 						small = small - (big - t);
		 						}
		 void BigToSmall()    { int t = small;  
								if (big+small < 3) 
									 small = big+small; 
								else small = 3; 
								big = big - (small - t);
								}
		 void pour() {
			 Random r = new Random();
			 while (big != 4) {
					switch (1 + r.nextInt(1119)%6) {
						case 1:
							FillSmallJug();
							break;
						case 2:
							FillBigJug();
							break;
						case 3:
							EmptySmallJug();
							break;
						case 4:
							EmptyBigJug();
							break;
						case 5:
							SmallToBig();
							break;
						case 6:
							BigToSmall();
							break;	 
					}
			}
		 }
	}

	class Main {
		public static void main(String[] args) {
			WaterJugs wj = new WaterJugs();
			wj.pour();
		}
	}
