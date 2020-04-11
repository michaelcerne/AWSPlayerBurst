package com.michaelcerne.awsplayerburst;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.concurrent.CompletableFuture;

public class AwsPlayerBurstController {

    public CompletableFuture<Boolean> isUnlimited() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        DescribeInstanceCreditSpecificationsRequest request = DescribeInstanceCreditSpecificationsRequest.builder()
                .instanceIds(new String[]{AwsPlayerBurst.ec2InstanceId})
                .build();

        CompletableFuture<DescribeInstanceCreditSpecificationsResponse> response = AwsPlayerBurst.ec2AsyncClient.describeInstanceCreditSpecifications(request)
                .whenComplete((res, err) -> {
                    completableFuture.complete(res.instanceCreditSpecifications().get(0).cpuCredits().equals("unlimited"));
                });

        response.join();

        return completableFuture;
    }

    public CompletableFuture<Boolean> setUnlimited(boolean value) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();

        InstanceCreditSpecificationRequest creditRequest = InstanceCreditSpecificationRequest.builder()
                .instanceId(AwsPlayerBurst.ec2InstanceId)
                .cpuCredits(value ? "unlimited" : "standard")
                .build();

        ModifyInstanceCreditSpecificationRequest request = ModifyInstanceCreditSpecificationRequest.builder()
                .instanceCreditSpecifications(creditRequest)
                .build();

        CompletableFuture<ModifyInstanceCreditSpecificationResponse> response = AwsPlayerBurst.ec2AsyncClient.modifyInstanceCreditSpecification(request)
                .whenComplete((res, err) -> {
                   completableFuture.complete(res.hasSuccessfulInstanceCreditSpecifications());
                });

        response.join();

        return completableFuture;
    }

    public void triggerPlayerCheck() {
        // must be delayed (onPlayerQuit is triggered before player truly disconnects)
        Bukkit.getScheduler().scheduleSyncDelayedTask(AwsPlayerBurst.plugin, () -> {
            boolean unlimitedState = Bukkit.getOnlinePlayers().size() > 0;
            setUnlimited(unlimitedState)
                    .whenComplete((res, data) -> {
                        if (res) {
                            AwsPlayerBurst.logger.info(String.format("Unlimited status: %s", unlimitedState ? "Enabled" : "Disabled"));
                        }
                    });
        }, 2);
    }
}
