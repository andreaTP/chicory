
Working ipotesis

Under specific compilation conditions, C2 applies an optimization to Long.compareUnsigned that rewrites the unsigned comparison using a signed transformation.
In this case the transformation’s assumptions are violated, leading to incorrect results for certain input values.
