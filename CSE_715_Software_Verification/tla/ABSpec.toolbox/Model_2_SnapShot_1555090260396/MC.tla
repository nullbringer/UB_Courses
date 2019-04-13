---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_1555090256370177000 == 
{ "abc", "lkh" , "jjh" }
----

\* PROPERTY definition @modelCorrectnessProperties:0
prop_1555090256370178000 ==
\A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 12 13:30:56 EDT 2019 by amlan
