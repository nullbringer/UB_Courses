---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_155631123019435000 == 
{ "abc", "lkh" , "jjh" }
----

\* PROPERTY definition @modelCorrectnessProperties:0
prop_155631123019438000 ==
\A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 26 16:40:30 EDT 2019 by amlan
