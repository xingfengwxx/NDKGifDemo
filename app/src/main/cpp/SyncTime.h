//
// Created by WangXingxing on 2020/5/20.
//

#ifndef NDKGIFDEMO_SYNCTIME_H
#define NDKGIFDEMO_SYNCTIME_H

#include <time.h>
#include "log.h"

class SyncTime {
public:
    void set_clock();

    unsigned int synchronize_time(int m_time);

private:
    timespec current_ts;
    timespec last_ts;
};


#endif //NDKGIFDEMO_SYNCTIME_H
