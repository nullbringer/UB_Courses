--------------------------- MODULE Simpleprogram ---------------------------
EXTENDS Integers
VARIABLES i, pc   

Init == (pc = "start") /\ (i = 0)

Pick == /\ pc = "start"  
        /\ i' \in 0..1000
        /\ pc' = "middle"

Add1 == /\ pc = "middle" 
        /\ i' = i + 1
        /\ pc' = "done"  
           
Next == Pick \/ Add1

=============================================================================
\* Modification History
\* Last modified Fri Mar 29 03:05:52 EDT 2019 by amlan
\* Created Fri Mar 29 03:04:34 EDT 2019 by amlan
