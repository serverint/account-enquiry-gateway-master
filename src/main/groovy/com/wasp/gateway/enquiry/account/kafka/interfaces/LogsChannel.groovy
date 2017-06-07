package com.wasp.gateway.enquiry.account.kafka.interfaces

import org.xnio.channels.MessageChannel
import org.springframework.cloud.stream.annotation.Output

/**
 * Created by aodunlami on 5/19/17.
 */
interface LogsChannel {

    @Output
    MessageChannel output()
}