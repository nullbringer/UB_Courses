---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_1555102230964195000 == 
{ "abc", "lkh" , "jjh" }
----

\* PROPERTY definition @modelCorrectnessProperties:0
prop_1555102230964196000 ==
\A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 12 16:50:30 EDT 2019 by amlan
