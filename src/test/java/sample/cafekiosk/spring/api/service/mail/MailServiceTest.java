package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 순수 mockito 사용하여 테스트
 */
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        //given

        // Mock용
//        when(mailSendClient.sendEmail(any(String.class),any(String.class),any(String.class),any(String.class)))
//                .thenReturn(true);

        //BDDMockito (위는 given절에 when이라서 헷갈릴수 있어서 탄생)
//        BDDMockito.given(mailSendClient.sendEmail(any(String.class),any(String.class),any(String.class),any(String.class)))
//                .willReturn(true);

        //Spy용
        doReturn(true)
                .when(mailSendClient)
                .sendEmail(anyString(),anyString(),anyString(),anyString());


        //when
        boolean result = mailService.sendMail("", "", "", "");

        //then
        assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}