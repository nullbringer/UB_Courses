---- MODULE MC ----
EXTENDS ABSpec, TLC

\* CONSTANT definitions @modelParameterConstants:0Data
const_1555052688417131000 == 
{ "abc", "lkh" , "jjh" }
----

\* INVARIANT definition @modelCorrectnessInvariants:1
inv_1555052688417133000 ==
\A v \in Data \X {0,1} : (AVar = v) ~> (BVar = v)
----
=============================================================================
\* Modification History
\* Created Fri Apr 12 03:04:48 EDT 2019 by amlan
