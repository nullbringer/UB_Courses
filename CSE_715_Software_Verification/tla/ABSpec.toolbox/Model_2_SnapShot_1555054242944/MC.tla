---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_1555053828122144000 == 
{ "abc", "lkh" , "jjh" }
----

\* PROPERTY definition @modelCorrectnessProperties:0
prop_1555053828122145000 ==
<> \A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 12 03:23:48 EDT 2019 by amlan
