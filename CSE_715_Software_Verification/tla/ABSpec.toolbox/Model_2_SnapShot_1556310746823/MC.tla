---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_155631074044315000 == 
{ "abc", "lkh" , "jjh" }
----

\* PROPERTY definition @modelCorrectnessProperties:0
prop_155631074044318000 ==
\A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 26 16:32:20 EDT 2019 by amlan
