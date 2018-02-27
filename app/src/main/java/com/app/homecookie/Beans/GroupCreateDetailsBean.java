package com.app.homecookie.Beans;

import java.util.List;

/**
 * Created by fluper-android on 27/7/17.
 */

public class GroupCreateDetailsBean {


        /**
         * groupDetails : {"groupName":"kitty","groupPhoto":"dummy.png","groupId":29}
         * memerDetails : [{"firstName":"ankur","lastName":"p","photo":"dummy.jpg","memberId":110},{"firstName":"Santosh","lastName":"joshi","photo":"dummy.jpg","memberId":112},{"firstName":"Pandey","lastName":"a","photo":"dummy.jpg","memberId":"111"}]
         */

        private GroupDetailsBean groupDetails;
        private List<MemerDetailsBean> memerDetails;


        public GroupDetailsBean getGroupDetails() {
            return groupDetails;
        }

        public void setGroupDetails(GroupDetailsBean groupDetails) {
            this.groupDetails = groupDetails;
        }

        public List<MemerDetailsBean> getMemerDetails() {
            return memerDetails;
        }

        public void setMemerDetails(List<MemerDetailsBean> memerDetails) {
            this.memerDetails = memerDetails;
        }

        public static class GroupDetailsBean {
            /**
             * groupName : kitty
             * groupPhoto : dummy.png
             * groupId : 29
             */

            private String groupName;
            private String groupPhoto;
            private String groupId;

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getGroupPhoto() {
                return groupPhoto;
            }

            public void setGroupPhoto(String groupPhoto) {
                this.groupPhoto = groupPhoto;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }
        }

        public static class MemerDetailsBean {
            /**
             * firstName : ankur
             * lastName : p
             * photo : dummy.jpg
             * memberId : 110
             */

            private String firstName;
            private String lastName;
            private String photo;
            private String memberId;

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }
        }

}
