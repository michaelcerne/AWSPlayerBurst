package com.michaelcerne.awsplayerburst;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;
import software.amazon.awssdk.services.ec2.*;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceCreditSpecificationsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceCreditSpecificationsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public final class AwsPlayerBurst extends JavaPlugin implements Listener {

    public static Logger logger = null;
    public static AwsPlayerBurst plugin = null;
    public static Ec2AsyncClient ec2AsyncClient = null;
    public static String ec2InstanceId = null;
    public static AwsPlayerBurstController controller = null;
    private static AwsPlayerBurstListener listener = null;

    @Override
    public void onEnable() {

        if (logger == null) {
            logger = this.getLogger();
        }

        if (plugin == null) {
            plugin = this;
        }

        if (ec2AsyncClient == null) {
            try {
                ec2AsyncClient = Ec2AsyncClient.create();
            } catch (Ec2Exception e) {
                logger.warning(String.format("Error from AWS: %s", e.getMessage()));
                logger.warning("Could not reach the EC2 API service.");
                logger.warning("Is the firewall allowing connections?");
                logger.warning("Are the credentials valid and properly configured?");
                logger.warning("Disabling.");
                this.getPluginLoader().disablePlugin(this);
            }
        }

        try {
            if (System.getenv("INSTANCE_OVERRIDE") == null) {
                ec2InstanceId = EC2MetadataUtils.getInstanceId();
            } else {
                ec2InstanceId = System.getenv("INSTANCE_OVERRIDE");
            }
            logger.info(String.format("Found current EC2 instance. ID: %s", ec2InstanceId));
        } catch (SdkClientException e) {
            logger.warning(String.format("Error from AWS: %s", e.getMessage()));
            logger.warning("Could not reach the EC2 metadata service.");
            logger.warning("Is the firewall allowing connections?");
            logger.warning("Are the credentials valid and properly configured?");
            logger.warning("Disabling.");
            this.getPluginLoader().disablePlugin(this);
        }

        if (controller == null) {
            controller = new AwsPlayerBurstController();
        }

        controller.triggerPlayerCheck();

        if (listener == null) {
            getServer().getPluginManager().registerEvents(new AwsPlayerBurstListener(), this);
            logger.info("Listening for players.");
        }
    }

    @Override
    public void onDisable() {
        if (ec2AsyncClient != null) {
            ec2AsyncClient.close();
        }
    }
}
