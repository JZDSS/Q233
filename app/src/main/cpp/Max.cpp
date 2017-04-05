//
// Created by Qi Yao on 17-4-4.
//

#include "Max.h"
int max(int* a)
{
    return *a > *(a+1) ? *a : *(a+1);
}