package com.tahweel.listener

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForwardFilterTest {
    private val trusted = ForwardRules(
        filter = true,
        senders = listOf("vf-cash", "e& money"),
        keywords = listOf("جنيه", "amount"),
    )

    @Test fun onlyExactTrustedSendersAreForwarded() {
        assertTrue(ForwardFilter.shouldForward(trusted, "VF-CASH", "Transfer received"))
        assertFalse(ForwardFilter.shouldForward(trusted, "fake-vf-cash", "جنيه 200"))
        assertFalse(ForwardFilter.shouldForward(trusted, "+201123456789", "amount 200"))
        assertFalse(ForwardFilter.shouldForward(trusted, "e& money support", "amount"))
    }

    @Test fun neverForwardWithoutValidRules() {
        assertFalse(ForwardFilter.shouldForward(null, "vf-cash", "200 EGP"))
        assertFalse(ForwardFilter.shouldForward(trusted.copy(senders = emptyList()), "vf-cash", "200 EGP"))
        assertFalse(ForwardFilter.shouldForward(trusted.copy(filter = false), "vf-cash", "200 EGP"))
    }

    @Test fun financialKeywordsDoNotOverrideAllowlist() {
        assertFalse(ForwardFilter.shouldForward(trusted, "Friend", "I owe you 200 جنيه"))
        assertTrue(ForwardFilter.shouldForward(trusted, "e& money", "رصيدك الحالي"))
    }
}
